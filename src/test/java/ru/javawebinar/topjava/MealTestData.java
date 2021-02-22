package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID_FALSE = 15;
    public static final int MEAL_ID_TRUE = 100002;
    public static final int NOT_FOUND = 10;

    public static final Meal NEW_MEAL = new Meal(MEAL_ID_TRUE, LocalDateTime.of(2020, Month.JUNE,
            20, 10,0), "завтрак",500);

    public static final Meal UPDATE_MEAL_ID_TRUE = new Meal(MEAL_ID_TRUE,
            LocalDateTime.of(2020,Month.JUNE,20, 10, 0 ), "обед", 800);

    public static final Meal UPDATE_MEAL_ID_FALSE = new Meal(MEAL_ID_FALSE,
            LocalDateTime.of(2020,Month.JUNE,20, 10, 0), "обед", 800);


    public static Meal getUpdated() {
        Meal uptated = new Meal(NEW_MEAL);

        uptated.setDateTime(LocalDateTime.of(2020, Month.JUNE, 20,10,0));
        uptated.setDescription("обед");
        uptated.setCalories(800);
        return uptated;
    }

}
