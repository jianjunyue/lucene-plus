package org.lucene.plus.demo;

import java.util.List;

import org.tensorflow.*;

public class WDL {

	private static Session sess = null;

	private void init() {
		try {
			String modelPath = "D:\\data\\model\\models\\wd\\5";
			SavedModelBundle bundle = SavedModelBundle.load(modelPath, "serve");
			sess = bundle.session();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public float[][] predict(float[][] features) {
		float[][] result = new float[features.length][1];
		try {
			Tensor<?> tensor_inputs = Tensor.create(features);
			List<Tensor<?>> tensor_preds = sess.runner().feed("serving_default_inputs:0", tensor_inputs)
					.fetch("StatefulPartitionedCall:0").run();
			if (tensor_preds != null && tensor_preds.size() > 0) {
				Tensor<?> tensor_pred = tensor_preds.get(0);
				tensor_pred.copyTo(result);
			}
			tensor_inputs.close();
			for (Tensor<?> tensor : tensor_preds) {
				tensor.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	public static void main(String[] args) {
		WDL wdl = new WDL();
		wdl.init();
		float f1[] = { 0.80154431f, 0.27216142f, -0.11624393f, -0.20231151f, -0.54305157f, -0.02103962f, -0.58976206f,
				-0.08241846f };
		float f2[] = { -0.29807281f, 0.35226166f, -0.10920508f, -0.25055521f, -0.03406402f, -0.006034f, 1.08055484f,
				-1.06113817f };
		float features[][] = { f1, f2, f2, f1 };
		float[][] result = wdl.predict(features);

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				System.out.println(result[i][j]);
			}
		}
	}

}
