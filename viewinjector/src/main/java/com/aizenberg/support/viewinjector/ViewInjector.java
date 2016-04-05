package com.aizenberg.support.viewinjector;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aizenberg.support.viewinjector.annotation.Click;
import com.aizenberg.support.viewinjector.annotation.Id;
import com.aizenberg.support.viewinjector.annotation.Layout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy Aizenberg
 */
public class ViewInjector {

    private Map<Integer, View> reflectedViews = new HashMap<>();

    private ViewInjector() {

    }

    public static View reflectLayout(Object clazz, LayoutInflater inflater, ViewGroup parent) {
        ViewInjector viewInjector = new ViewInjector();
        View view = viewInjector.reflectLayoutInternal(clazz, inflater, parent);
        viewInjector.reflectInternal(clazz, view);
        return view;
    }

    public static void reflect(Object clazz, View view) {
        new ViewInjector().reflectInternal(clazz, view);
    }

    public static void reflect(Object clazz, Activity activity) {
        new ViewInjector().reflectInternal(clazz, activity.getWindow().getDecorView());
    }

    public View reflectLayoutInternal(Object clazz, LayoutInflater inflater, ViewGroup parent) {
        Layout annotation = clazz.getClass().getAnnotation(Layout.class);
        int id = annotation.value();
        return inflater.inflate(id, parent, false);
    }

    public void reflectInternal(final Object clazz, View view) {
        reflectedViews.clear();

        Field[] declaredFields = clazz.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    if (!field.isAccessible()) field.setAccessible(true);
                    Id annotation = field.getAnnotation(Id.class);
                    int viewId = annotation.value();
                    View value = view.findViewById(viewId);
                    field.set(clazz, value);
                    String method = annotation.clickMethod();
                    if (method != null && !method.isEmpty()) {
                        try {
                            setClickListener(clazz, value, method);
                        } catch (NoSuchMethodException e) {
                            throw new InjectionException("Method " + method + " not found in " + clazz.getClass().getName());
                        }
                    } else {
                        reflectedViews.put(viewId, value);
                    }
                } catch (Throwable throwable) {
                    throw new InjectionException("Unable to inject " + field.getName(), throwable);
                }
            }
        }
        Method[] methods = clazz.getClass().getDeclaredMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(Click.class)) {
                Click annotation = method.getAnnotation(Click.class);
                int value = annotation.value();
                final View targetView = reflectedViews.get(value);
                if (targetView == null) {
                    reflectedViews.clear();
                    throw new InjectionException("View for click method " + method.getName() + " with id " + value + " not found");
                }
                targetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            method.invoke(clazz, targetView);
                        } catch (IllegalAccessException e) {
                            throw new InjectionException("IllegalAccessException: Method " + method.getName() + " can't be invoked. " + clazz.getClass().getName(), e);
                        } catch (InvocationTargetException e) {
                            throw new InjectionException("InvocationTargetException: Method " + method.getName() + " can't be invoked. " + clazz.getClass().getName(), e);
                        }

                    }
                });
            }
        }
        reflectedViews.clear();
    }

    private void setClickListener(final Object clazz, final View view, final String methodName) throws NoSuchMethodException {
        final Method declaredMethod = clazz.getClass().getDeclaredMethod(methodName, View.class);
        if (!declaredMethod.isAccessible()) declaredMethod.setAccessible(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    declaredMethod.invoke(clazz, view);
                } catch (Exception e) {
                    throw new InjectionException("Method " + methodName + " not found in " + clazz.getClass().getName(), e);
                }
            }
        });
    }

}
