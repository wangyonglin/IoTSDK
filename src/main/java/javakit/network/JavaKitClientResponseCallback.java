package javakit.network;

public interface JavaKitClientResponseCallback<T> {
    default void success(T res){};
    default void failure(Exception e){};
}
