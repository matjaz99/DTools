package si.matjazcerkvenik.dtools.io.statistics;

public class Test {
	
	public static void main(String[] args) {
		
		float avg = 0.0f;
		int a = 1;
		int b = 2;
		int c = 2;
		int d = 3;
		
		avg = (a + b + c + d) / 4f;
		int avgI = Math.round(avg);
		
		System.out.println(avg);
		
	}
	
}
