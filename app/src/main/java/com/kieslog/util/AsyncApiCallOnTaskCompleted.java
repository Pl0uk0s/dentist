package com.kieslog.util;

import android.os.Bundle;

public interface AsyncApiCallOnTaskCompleted {
    void onTaskCompleted(int thread, String result);
    void onTaskCompleted(int thread, Bundle vars, String result);
}
