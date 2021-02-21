package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;


    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("datetime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories());

        if (meal.isNew()) {
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
            return meal;
        } else if (jdbcTemplate.queryForObject("SELECT user_id FROM user_meal WHERE id=?", Integer.class, meal.getId())
                != null && Integer.valueOf(userId).equals(jdbcTemplate.queryForObject("SELECT user_id FROM user_meal " +
                "WHERE id=?", Integer.class, meal.getId()))) {
            namedParameterJdbcTemplate.update("UPDATE user_meal SET date_time=:datetime, description=:description," +
                    "calories=:calories  WHERE id=:id", map);
            return meal;

        }

        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (jdbcTemplate.queryForObject("SELECT user_id FROM user_meal WHERE id=?", Integer.class, id) != null &&
                Integer.valueOf(userId).equals(jdbcTemplate.queryForObject("SELECT user_id FROM user_meal WHERE id=?",
                        Integer.class, id))) {
            return jdbcTemplate.update("DELETE FROM user_meal WHERE id=?", id) != 0;
        }

        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (jdbcTemplate.queryForObject("SELECT user_id FROM user_meal WHERE id=?", Integer.class, id) != null &&
                Integer.valueOf(userId).equals(jdbcTemplate.queryForObject("SELECT user_id FROM user_meal WHERE id=?",
                        Integer.class, id))) {
            List<Meal> user_meal = jdbcTemplate.query("SELECT * FROM user_meal WHERE id=?", ROW_MAPPER, id);
            return DataAccessUtils.singleResult(user_meal);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> mealList = jdbcTemplate.query("SELECT * FROM user_meal WHERE user_id=?", ROW_MAPPER, userId);
        return mealList.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        List<Meal> mealFilter = getAll(userId);
        return mealFilter.stream()
                .filter(meal -> meal.getDateTime().isAfter(startDateTime) && meal.getDateTime().isBefore(endDateTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}
