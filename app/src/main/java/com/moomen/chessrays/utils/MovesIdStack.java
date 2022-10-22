package com.moomen.chessrays.utils;

import com.moomen.chessrays.model.StackGameModel;

import java.util.Stack;

public class MovesIdStack {
    public MovesIdStack() {
    }

    public static Stack<StackGameModel> moveIdStack = new Stack<>();

    public static void pushStack(StackGameModel stackGameModel) {
        moveIdStack.push(stackGameModel);
    }
    public static StackGameModel popStack() {
        return moveIdStack.pop();
    }
    public static StackGameModel peekStack() {
        return moveIdStack.peek();
    }

    public static Stack<StackGameModel> getMoveIdStack() {
        return moveIdStack;
    }

    public static void setMoveIdStack(Stack<StackGameModel> moveIdStack) {
        MovesIdStack.moveIdStack = moveIdStack;
    }
}
