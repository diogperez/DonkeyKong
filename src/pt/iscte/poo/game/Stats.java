package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Stats{
	
	private static final int TOP = 10;
	private static List<Player> highscores;

	public Stats() {
		highscores = new ArrayList<>();
	}
	
	public void readStats() {
		try {
			Scanner sc = new Scanner ( new File("Highscores.txt"));
			sc.nextLine();
			while(sc.hasNextLine()) {
				String[] parts = sc.nextLine().split(":");
				highscores.add(new Player(parts[1].trim(), Integer.parseInt(parts[2].trim())));
			}
			sc.close();
			
		}catch(FileNotFoundException e) {
			System.err.println("Ficheiro de Scores não encontrado!");
		}
	}
	
	public String updateHighscores(Player player) {
		String top10 = "";
		try {
			readStats();
			highscores.add(player);
			Collections.sort(highscores);
			FileWriter fw = new FileWriter("Highscores.txt");
			fw.write("Ranking : Nome : Tempo\n");
			top10 += "Ranking : Nome : Tempo\n";
			for(int i = 0; i < Math.min(TOP, highscores.size()); i++) {
				Player p = highscores.get(i);
				fw.write((i +1) + " : " + p.getName() + " : " + p.getTime() + "\n");
				top10 += ((i + 1) + " : " + p.getName() + " : " + p.getTime() + "\n");
			}
			fw.close();
			
		}catch(IOException e) {
			System.err.println("Erro a escrever no ficheiro de Scores");
		}
		return top10;
	}
	
}
