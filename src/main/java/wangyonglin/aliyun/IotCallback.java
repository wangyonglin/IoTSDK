package wangyonglin.aliyun;

public interface IotCallback<T>
{
    void successful(T obj);
    void failure(Exception e);
}
