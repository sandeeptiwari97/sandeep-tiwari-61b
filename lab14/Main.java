import lab14lib.*;

public class Main {
	public static void main(String[] args) {
		/** Your code here. */
		Generator generator = new AcceleratingSawToothGenerator(200, 1.1);
		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
		gav.drawAndPlay(4096, 1000000);
	}
}