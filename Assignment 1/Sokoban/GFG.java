import java.util.*; 
  
class GFG { 
    public static void main (String[] args) { 
        Deque<Character> stack = new ArrayDeque<Character>(); 
        stack.push('A'); 
        stack.push('B'); 
        System.out.println(stack.peek()); 
        System.out.println(stack.pop()); 
        System.out.println(stack.peek()); 
    } 
} 