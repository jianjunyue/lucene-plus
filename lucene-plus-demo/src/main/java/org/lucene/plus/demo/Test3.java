package org.lucene.plus.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.tensorflow.*;

public class Test3 {

	public static void main(String[] args) {
		System.out.println(TensorFlow.version());
		String export_path = "C:\\Users\\lejianjun\\git\\lucene-plus\\lucene-plus-demo\\src\\main\\java\\org\\lucene\\plus\\demo\\wd\\0.1";

		SavedModelBundle bundle = SavedModelBundle.load(export_path, "serve");
		Session sess = bundle.session();
		float f1[] = { 1.0f, 2.0f };
		float f2[] = { 2.0f, 3.0f };
		float f3[][] = { f1, f2, f1, f2 };
		Tensor x = Tensor.create(f3);
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
			System.out.println("----------- i:"+i+" -------------");
			for (int j = 0; j < result[i].length; j++) {
				System.out.println(result[i][j]);
			}
		}
	}

}
