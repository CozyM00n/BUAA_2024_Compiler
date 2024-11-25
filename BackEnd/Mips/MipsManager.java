package BackEnd.Mips;

import BackEnd.Mips.ASM.Asm;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MipsManager {
    private static MipsManager mipsManager = new MipsManager();
    public static MipsManager getInstance() {
        return mipsManager;
    }

    private ArrayList<Asm> dataSeg;
    private ArrayList<Asm> textSeg;
    private MipsManager() {
        this.dataSeg = new ArrayList<>();
        this.textSeg = new ArrayList<>();
    }

    public void addToData(Asm asm) {
        dataSeg.add(asm);
    }

    public void addToText(Asm asm) {
        textSeg.add(asm);
    }


    public String genMipsCode() {
        StringBuilder sb = new StringBuilder(".data\n\t");
        sb.append(dataSeg.stream().map(Asm::toString).
                collect(Collectors.joining("\n\t")));
        sb.append("\n");
        sb.append(textSeg.stream().map(Asm::toString)
                .collect(Collectors.joining("\n")));
        return sb.toString();
    }
}
