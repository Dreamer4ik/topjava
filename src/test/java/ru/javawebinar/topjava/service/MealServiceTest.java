package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.MealTestData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Qualifier("mealService")
    @Autowired
    private MealService service;

    @Qualifier("jdbcMealRepository")
    @Autowired
    private MealRepository repository;

//          method get

    @Test
    public void testGetTrueMealTrueUser() {
        Meal meal = service.get(MealTestData.MEAL_ID_TRUE, UserTestData.USER_ID);
        assertThat(meal).isEqualTo(MealTestData.NEW_MEAL);
    }

    @Test
    public void testGetMealIdFalseUserIdTrue() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.MEAL_ID_FALSE, UserTestData.USER_ID));
    }

    @Test
    public void testGetMealIdTrueUserIdFalse() {
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.MEAL_ID_TRUE, UserTestData.ADMIN_ID));
    }

//          method delete

    @Test
    public void testDeleteMealIdTrueUserIdTrue() {
        service.delete(MealTestData.MEAL_ID_TRUE, UserTestData.USER_ID);
        assertNull(repository.get(MealTestData.MEAL_ID_TRUE, UserTestData.USER_ID));
    }

    @Test
    public void testDeleteMealIdFalseUserIdTrue() {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.MEAL_ID_FALSE, UserTestData.USER_ID));
    }

    @Test
    public void testDeleteMealIdTrueUserIdFalse() {
        service.delete(MealTestData.MEAL_ID_TRUE, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.MEAL_ID_TRUE, UserTestData.ADMIN_ID));
    }

//          method update

    @Test
    public void testUpdateMealIdTrueUserIdTrue() {
        service.update(MealTestData.getUpdated(), UserTestData.USER_ID);
        assertThat(MealTestData.UPDATE_MEAL_ID_TRUE).isEqualTo(MealTestData.getUpdated());
    }

    @Test
    public void testUpdateMealIdFalseUserIdTrue() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.UPDATE_MEAL_ID_FALSE, UserTestData.USER_ID));
    }

    @Test
    public void testUpdateMealIdTrueUserIdFalse() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.UPDATE_MEAL_ID_TRUE, UserTestData.ADMIN_ID));
    }
}