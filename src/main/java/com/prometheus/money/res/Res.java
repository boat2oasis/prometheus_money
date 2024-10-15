package com.prometheus.money.res;

public class Res<T> {
	private int status; // 响应状态码
	private String message; // 响应消息
	private T data; // 响应数据（泛型，可以是任意类型）

	private static final int SUCCESS = 200;
	private static final int FAIL = 500;

	public Res(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	// 静态的 success 方法
	public static <T> Res<T> success(T data) {
		return new Res<>(SUCCESS, "Success", data); // 200 通常表示成功
	}

	// 静态的 fail 方法
	public static <T> Res<T> fail(String message) {
		return new Res<>(FAIL, message, null); // 500 表示服务器错误，或自定义错误码
	}

	// Getter和Setter方法
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	// 重写toString方法
	@Override
	public String toString() {
		return "Res{" + "status=" + status + ", message='" + message + '\'' + ", data=" + data + '}';
	}
}
