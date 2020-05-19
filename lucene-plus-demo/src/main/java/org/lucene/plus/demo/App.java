package org.lucene.plus.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App {
	private final static ExecutorService executorPool = new ThreadPoolExecutor(50, 500, 5, TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(1000));

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CompletableFuture<String> asyncFuture = asyncSearch(1);
		System.out.println(asyncFuture.get());
		System.out.println("Hello World!");

		toBytes(1000);
		intToSortableBytes(1000);
		floatToSortableInt(6.6f);

		System.out.println("6 >> 2 ===== " + (3000 >> 8));
	}

	public static CompletableFuture<String> asyncSearch(int para) {
		try {
			CompletableFuture<String> asyncFuture = CompletableFuture.supplyAsync(() -> search(para), executorPool);
			return asyncFuture;
		} catch (Exception e) {
			System.out.println("ItemSearchService asyncSearch is error:" + e.getMessage());
		}
		return null;
	}

	private static String search(int para) {
		return "para:" + para;
	}

	public static byte[] toBytes(int number) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) number;
		bytes[1] = (byte) (number >> 8);
		bytes[2] = (byte) (number >> 16);
		bytes[3] = (byte) (number >> 24);
		return bytes;
	}

	public static void intToSortableBytes(int value) {
		byte[] bytes = new byte[4];
		// Flip the sign bit, so negative ints sort before positive ints correctly:
		value ^= 0x80000000;
		bytes[0] = (byte) (value >> 24);
		bytes[1] = (byte) (value >> 16);
		bytes[2] = (byte) (value >> 8);
		bytes[3] = (byte) value;
	}

	public static void floatToSortableInt(float value) {
		int i = Float.floatToIntBits(value);
		System.out.println(i);
		System.out.println(sortableFloatBits(i));
	}

	public static int sortableFloatBits(int bits) {
		return bits ^ (bits >> 31) & 0x7fffffff;
	}

}
