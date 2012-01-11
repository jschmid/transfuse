package org.androidrobotics.example.simple;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import org.androidrobotics.annotations.*;

import javax.inject.Inject;

@Activity("SimpleActivity")
@Layout(R.layout.main)
public class SimpleActivityDelegate {

    private boolean onCreateCalled = false;
    private boolean secondOnCreatCalled = false;

    @Inject
    private ProvidedValue providedValue;
    @Inject
    public SimpleController controller;
    private LifecycleLogger value;
    @Inject
    @SystemService(Context.AUDIO_SERVICE)
    private Object systemService;
    private LateReturnListener lateReturnListener;
    @Inject
    @View(R.id.asynchActivity)
    private Button button;

    @Inject
    public SimpleActivityDelegate(LifecycleLogger value) {
        this.value = value;
    }

    @OnCreate
    public void registerLateReturnListener() {
        button.setOnClickListener(lateReturnListener);
    }

    @OnCreate
    public void callMe() {
        Log.i("info", "Called");
        onCreateCalled = true;
    }

    @OnCreate
    public void anotherCall() {
        Log.i("test", "test");
        secondOnCreatCalled = true;
    }

    @Inject
    private void setLateReturnListener(LateReturnListener lateReturnListener) {
        this.lateReturnListener = lateReturnListener;
    }

    public boolean isOnCreateCalled() {
        return onCreateCalled;
    }

    public boolean isSecondOnCreatCalled() {
        return secondOnCreatCalled;
    }

    public SimpleController getController() {
        return controller;
    }

    public boolean isConstructorInjected() {
        return value != null;
    }

    public boolean isSystemServiceInjected() {
        return systemService != null;
    }

    public ProvidedValue getProvidedValue() {
        return providedValue;
    }
}
