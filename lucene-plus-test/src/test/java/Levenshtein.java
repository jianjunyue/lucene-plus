 
public class Levenshtein {
	
	public static void main(String[] args) {
		String A ="必胜客宅急送A（(";// "abcdefg";
		String B ="必胜客宅急a";//"abcdef";
		System.out.println( levenshtein(A,B));
	}

	public static float levenshtein(String str1, String str2) {
		if(str1==null||str2==null||str1.length()==0||str2.length()==0){
			return 0f;
		}
		// 计算两个字符串的长度。
		int len1 = str1.length();
		int len2 = str2.length();
		// 建立上面说的数组，比字符长度大一个空间
		int[][] dif = new int[len1 + 1][len2 + 1];
		// 赋初值，步骤B。
		for (int a = 0; a <= len1; a++) {
			dif[a][0] = a;
		}
		for (int a = 0; a <= len2; a++) {
			dif[0][a] = a;
		}
		// 计算两个字符是否一样，计算左上的值
		int temp;
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) { 
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					temp = 0;
				} else {
					temp = 1;
				}
				// 取三个值中最小的
				dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
						dif[i - 1][j] + 1); 
			}
		} 
		// 计算相似度
		float similarity = 1 - (float) dif[len1][len2]
				/ Math.max(str1.length(), str2.length()); 
		return similarity;
	}
	
	public static int min(int a, int b, int c) {
		int i = a < b ? a : b;
		return i = i < c ? i : c;
	}
}
