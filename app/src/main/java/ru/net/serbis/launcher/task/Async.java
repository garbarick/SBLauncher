package ru.net.serbis.launcher.task;

public interface Async
{
    void preExecute();
    void background();
    void postExecute();
}
