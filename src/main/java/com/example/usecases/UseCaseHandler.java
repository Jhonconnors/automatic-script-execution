package com.example.usecases;

import com.example.exception.InvalidScriptException;

public interface UseCaseHandler <I,O>{

    O execute(I request) throws InvalidScriptException;
}
