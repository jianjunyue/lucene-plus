package org.lucene.plus.demo;

import org.tensorflow.*;

public class Test1 {

	public static void main(String[] args) {
		System.out.println(TensorFlow.version());
		String export_path = "D:\\data\\model\\models\\wd\\5";

		SavedModelBundle bundle = SavedModelBundle.load(export_path, "serve");
		Session sess = bundle.session();
		float f1[] = { 0.80154431f, 0.27216142f, -0.11624393f, -0.20231151f, -0.54305157f, -0.02103962f, -0.58976206f,
				-0.08241846f };
		float f2[] = { -0.29807281f, 0.35226166f, -0.10920508f, -0.25055521f, -0.03406402f, -0.006034f, 1.08055484f,
				-1.06113817f };
		float f3[][] = { f1, f2, f2 };
		Tensor<?> x = Tensor.create(f3);
		Tensor<?> y = sess.runner().feed("serving_default_inputs:0", x).fetch("StatefulPartitionedCall:0").run().get(0);
		float[][] result = new float[f3.length][1];
		y.copyTo(result);
		if (x != null)
			x.close();
		if (y != null)
			y.close();
		if (sess != null)
			sess.close();
		if (bundle != null)
			bundle.close();

		for (int i = 0; i < result.length; i++) {
			System.out.println("----------- i:" + i + " -------------");
			for (int j = 0; j < result[i].length; j++) {
				System.out.println(result[i][j]);
			}
		}
	}

}
