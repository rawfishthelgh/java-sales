package com.example.sales.batch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.sales.monthly.monthlySalesResult.MonthlySalesResult;
import com.example.sales.monthly.monthlySalesResult.MonthlySalesResultRepository;
import com.example.sales.monthly.monthlyStockTranSum.MonthlyStockTranSum;
import com.example.sales.plant.Plant;
import com.example.sales.plant.StoreRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MonthlySalesBatchConfiguration {

	private final int CHUNK_SIZE = 10;

	private final DataSource dataSource;
	private final EntityManagerFactory entityManagerFactory;
	private final EntityManager entityManager;
	@Bean
	public Job monthlySalesJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("monthlySalesJob", jobRepository)
			.start(monthlySalesSumStep(jobRepository, transactionManager))
			.next()
			.build();
	}
	@Bean
	public Step monthlySalesResultStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

		return new StepBuilder("monthlySalesResultStep", jobRepository)
			.<Long, MonthlySalesResult>chunk(CHUNK_SIZE, transactionManager)
			.reader(plantIdReader())
			.build();
	}

	@Bean
	public Step monthlySalesSumStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

		return new StepBuilder("monthlySalesSumStep", jobRepository)
			.<Long, MonthlyStockTranSum>chunk(CHUNK_SIZE, transactionManager)
			.reader(plantIdReader())
			.processor(monthlyStockProcessor())
			.writer(monthlyStockWriter())
			.build();
	}



	@Bean
	public ItemReader<Long> plantIdReader() {
		return new JpaPagingItemReaderBuilder<Long>()
			.name("plantIdReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("SELECT DISTINCT s.plant.id FROM Store s")
			.pageSize(CHUNK_SIZE)
			.build();
	}

	@Bean
	public ItemProcessor<Long, MonthlyStockTranSum> monthlySalesResultProcessor() {
		return plantId -> {
			log.info("Processing plantId: {}", plantId);
			YearMonth currentMonth = YearMonth.now().minusMonths(1);
			YearMonth previousMonth = currentMonth.minusMonths(1);

			BigDecimal revenue; // 매출액

			BigDecimal costOfGoodsSold; // 매출원가

			BigDecimal grossProfit; //매출총이익

			BigDecimal sellingGeneralAndAdministrativeExpenses; //판관비

			BigDecimal operatingProfit; //영업이익
		}
	}

	@Bean
	public ItemProcessor<Long, MonthlyStockTranSum> monthlyStockProcessor() {
		return plantId -> {
			log.info("Processing plantId: {}", plantId);

			YearMonth currentMonth = YearMonth.now().minusMonths(1);
			YearMonth previousMonth = currentMonth.minusMonths(1);
			log.info("currentMonth = " + currentMonth);
			log.info("previousMonth = " + previousMonth);

			BigDecimal beginInventoryAmount = entityManager.createQuery(
					"SELECT m.endInventoryAmount " +
						"FROM MonthlyStockTranSum m " +
						"WHERE m.plant.id = :plantId AND m.yearMonth = :previousMonth", BigDecimal.class)
				.setParameter("plantId", plantId)
				.setParameter("previousMonth", previousMonth.atDay(1))
				.getSingleResult();

			System.out.println("beginInventoryAmount = " + beginInventoryAmount);

			BigDecimal monthlyPurchaseAmount = entityManager.createQuery(
					"SELECT COALESCE(SUM(s.quantity * m.purchasePrice), 0) " +
						"FROM StockTrans s JOIN Material m ON s.materialId = m.id " +
						"WHERE s.stockTransType = 'IN' AND s.plantId = :plantId AND s.stockTransDateTime BETWEEN :start AND :end",
					BigDecimal.class)
				.setParameter("plantId", plantId)
				.setParameter("start", currentMonth.atDay(1).atStartOfDay())
				.setParameter("end", currentMonth.atEndOfMonth().atTime(23, 59, 59))
				.getSingleResult();

			BigDecimal costOfGoodsSold = entityManager.createQuery(
					"SELECT COALESCE(SUM(s.quantity * m.purchasePrice), 0) " +
						"FROM StockTrans s JOIN Material m ON s.materialId = m.id " +
						"WHERE s.stockTransType = 'OUT' AND s.moveType = 'STORE_SALES' " +
						"AND s.plantId = :plantId AND s.stockTransDateTime BETWEEN :start AND :end", BigDecimal.class)
				.setParameter("plantId", plantId)
				.setParameter("start", currentMonth.atDay(1).atStartOfDay())
				.setParameter("end", currentMonth.atEndOfMonth().atTime(23, 59, 59))
				.getSingleResult();

			BigDecimal endInventoryAmount = beginInventoryAmount.add(monthlyPurchaseAmount).subtract(costOfGoodsSold);

			return MonthlyStockTranSum.builder()
				.yearMonth(currentMonth.atDay(1))
				.beginInventoryAmount(beginInventoryAmount)
				.endInventoryAmount(endInventoryAmount)
				.monthlyPurchaseAmount(monthlyPurchaseAmount)
				.costOfGoodsSold(costOfGoodsSold)
				.plant(Plant.builder()
					.id(plantId)
					.build())
				.build();
		};
	}

	@Bean
	public ItemWriter<MonthlyStockTranSum> monthlyStockWriter() {
		return items -> {
			for (MonthlyStockTranSum item : items) {
				entityManager.persist(item);
				log.info("Saved MonthlyStockTranSum: {}", item);
			}
		};
	}

}
