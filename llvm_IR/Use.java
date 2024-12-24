package llvm_IR;

import llvm_IR.llvm_Values.Value;

public class Use {
    private User user;
    private Value used;

    public Use(User user, Value used) {
        this.user = user;
        this.used = used;
    }

    public User getUser() {
        return user;
    }

    public Value getUsed() {
        return used;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUsed(Value used) {
        this.used = used;
    }
}
