package com.example.e610.popularmoviesstage2.Utils;


public interface NetworkResponse {


    void OnSuccess(String JsonData);
    void OnUpdate(boolean IsDataReceived);
}
