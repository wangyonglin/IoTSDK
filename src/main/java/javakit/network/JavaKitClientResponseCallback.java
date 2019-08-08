package javakit.network;

public interface JavaKitClientResponseCallback<T> {
     void success(T res);
     void failure(Exception e);
}
