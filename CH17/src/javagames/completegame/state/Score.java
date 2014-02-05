package javagames.completegame.state;

public class Score implements Comparable<Score> {
	
	public String name = "";
	public int score = 0;

	public Score(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public int compareTo(Score o) {
		if (score == o.score)
			return 0;
		if (score < o.score)
			return -1;
		return 1;
	}
	
}