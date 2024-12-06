package BackEnd.Mips.ASM;

public class Comment extends Asm {
    private String content;

    public Comment(String content) {
        this.content = "#\t\t\t\t\t" + content;
    }

    @Override
    public String toString() {
        return content;
    }
}
