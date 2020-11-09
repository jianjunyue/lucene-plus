
public class Test {

	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		for (int i = 0; i < 2000; i++) {
			String str1 = "必胜客宅急送A（(" + i;// "abcdefg";
			String str2 = i + "必胜客宅急a";// "abcdef";
			Levenshtein.levenshtein(str1, str2);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		
		  start = System.currentTimeMillis();
		for (int i = 2000; i < 4000; i++) {
			String str1 = "【开箱测评】冠军 /championairfilter（底级） 5 星"  ;// "abcdefg";
			String str2 =   "【开箱测评】车洁邦 gongnxj（底级） 5 星";// "abcdef";
			Levenshtein.levenshtein(str1, str2);
		}
		  end = System.currentTimeMillis();
		System.out.println(end - start);
		

		  start = System.currentTimeMillis();
		for (int i = 0; i < 25000; i++) {
			String str1 = "星"+1  ;// "abcdefg";
			String str2 =  1+ "车";// "abcdef";
			str1.equals(str2);
		}
		  end = System.currentTimeMillis();
		System.out.println(end - start);
	}

}
