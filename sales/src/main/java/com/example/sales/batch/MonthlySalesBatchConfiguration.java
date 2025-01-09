package com.example.sales.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.example.sales.stockTrans.MoveType;
import com.example.sales.stockTrans.StockTrans;
import com.example.sales.stockTrans.StockTransType;
import com.example.sales.util.DateUtil;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MonthlySalesBatchConfiguration {

	private final DataSource dataSource;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public JdbcCursorItemReader<StockTrans> stockTransItemReader() {

		JdbcCursorItemReader<StockTrans> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);

		YearMonth previousMonth = DateUtil.getPreviousMonth();
		String yearMonthCondition = previousMonth.toString();

		String sql =
			"SELECT id, stock_trans_type, move_type, plant_id, target_plant_id, material_id, quantity, stock_trans_date_time "
				+
				"FROM stock_trans " +
				"WHERE DATE_FORMAT(stock_trans_date_time, '%Y-%m') = ?";

		reader.setSql(sql);
		reader.setPreparedStatementSetter(ps -> ps.setString(1, yearMonthCondition));

		reader.setRowMapper(new StockTransRowMapper());
		return reader;
	}

	public static class StockTransRowMapper implements RowMapper<StockTrans> {
		@Override
		public StockTrans mapRow(ResultSet rs, int rowNum) throws SQLException {

			StockTrans stockTrans = StockTrans.builder()
				.id(rs.getLong("id"))
				.stockTransType(StockTransType.valueOf(rs.getString("stock_trans_type")))
				.moveType(MoveType.valueOf(rs.getString("move_type")))
				.plantId(rs.getLong("plant_id"))
				.targetPlantId(rs.getLong("target_plant_id"))
				.materialId(rs.getLong("material_id"))
				.quantity(rs.getBigDecimal("quantity"))
				.stockTransDateTime(rs.getTimestamp("stock_trans_date_time").toLocalDateTime())
				.build();

			return stockTrans;
		}
	}

}
