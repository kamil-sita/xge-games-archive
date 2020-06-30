package game;

public class TeamRaport {
    public int countGunRed = 0;
    public int countGunGreen = 0;
    public int countGunBlue = 0;
    public int countBodyRed = 0;
    public int countBodyGreen = 0;
    public int countBodyBlue = 0;

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("+++++++++++ \n");
        s.append("body \n");
        s.append("r " + countBodyRed + "\n");
        s.append("g " + countBodyGreen + "\n");
        s.append("b " + countBodyBlue + "\n");
        s.append("gun \n");
        s.append("r " + countGunRed + "\n");
        s.append("g " + countGunGreen + "\n");
        s.append("b " + countGunBlue + "\n");
        return s.toString();
    }
}
