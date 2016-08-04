package com.wl.dudian.app.repository;

import com.wl.dudian.app.model.StartImage;
import com.wl.dudian.app.viewmodel.StartImageVM;

import rx.functions.Func1;
import rx.schedulers.Timestamped;

/**
 * Created by Qiushui on 16/8/3.
 */
public class StartImageMapping implements Func1<Timestamped<StartImage>, StartImageVM>{

    private static StartImageMapping instance;

    public static StartImageMapping instance() {
        if (instance == null) {
            instance = new StartImageMapping();
        }
        return instance;
    }

    @Override
    public StartImageVM call(Timestamped<StartImage> startImageTimestamped) {
        StartImage startImage = startImageTimestamped.getValue();
        return new StartImageVM(startImage.getText(), startImage.getImg());
    }
}
