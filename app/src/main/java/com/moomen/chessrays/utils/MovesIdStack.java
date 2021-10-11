package com.moomen.chessrays.utils;

import com.moomen.chessrays.model.OpeningModel;

import java.util.Stack;

public class MovesIdStack {
    public MovesIdStack() {
    }

    public static Stack<OpeningModel> moveIdModlesStack = new Stack<>();

    public static void pushStack(OpeningModel openingModel) {
        moveIdModlesStack.push(openingModel);
    }
    public static OpeningModel popStack() {
        return moveIdModlesStack.pop();
    }
    public static OpeningModel peekStack() {
        return moveIdModlesStack.peek();
    }

    public static Stack<OpeningModel> getMoveIdModlesStack() {
        return moveIdModlesStack;
    }

    public static void setMoveIdModlesStack(Stack<OpeningModel> moveIdModlesStack) {
        MovesIdStack.moveIdModlesStack = moveIdModlesStack;
    }
}
