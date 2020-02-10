package org.reactnative.camera.events;

import androidx.core.util.Pools;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import org.reactnative.camera.CameraViewManager;

import java.util.Formatter;

public class CardReadEvent extends Event<CardReadEvent> {
    private static final Pools.SynchronizedPool<CardReadEvent> EVENTS_POOL =
            new Pools.SynchronizedPool<>(3);

    private int[][] mCardPoints;
    private int mWidth;
    private int mHeight;


    private CardReadEvent() {}

    public static CardReadEvent obtain(int viewTag, int[][] cardPoints, int width, int height) {
        CardReadEvent event = EVENTS_POOL.acquire();
        if (event == null) {
            event = new CardReadEvent();
        }
        event.init(viewTag, cardPoints, width, height);
        return event;
    }

    private void init(int viewTag, int[][] cardPoints, int width, int height) {
        super.init(viewTag);
        mCardPoints = cardPoints;
        mWidth = width;
        mHeight = height;
    }
    
    
    @Override
    public String getEventName() {

        return CameraViewManager.Events.EVENT_ON_CARD_READ.toString();
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
    }

    private WritableMap serializeEventData() {
        WritableMap event = Arguments.createMap();
        WritableArray points = Arguments.createArray();
        for(int[] point : mCardPoints){
            WritableMap position = Arguments.createMap();
            position.putInt("x",point[0]);
            position.putInt("y",point[1]);
            points.pushMap(position);
        }
        event.putArray("points", points);
        event.putInt("width", mWidth);
        event.putInt("height", mHeight);

       return event;
    }
}
