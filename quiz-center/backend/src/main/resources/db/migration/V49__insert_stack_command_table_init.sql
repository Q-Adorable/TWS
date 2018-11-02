INSERT INTO stackCommand(name,compile,execute,sourcePostfix,templateCode,testcase,executePostfix)
VALUES (
"java",
"javac",
"java",
".java",
"public class demo {
    // change the function here
    public static String func(String a){
        return a;
    }
    public static void main(String[] args) {
        // change here
        String result = func(args[0]);
        System.out.println(result);
    }
}",
"[
    {
        \"input\":\"a\",
        \"expectedOutput\":\"b\"
    },
    {
        \"input\":\"c\",
        \"expectedOutput\":\"d\"
    }
]",
""
);



INSERT INTO stackCommand(name,compile,execute,sourcePostfix,templateCode,testcase,executePostfix)
VALUES (
"python",
"",
"python",
".py",
"import sys

def method():
    # change here
    para = sys.argv[1]
    print(para)

if __name__ == \"__main__\":
    method()
",
"[
    {
        \"input\":\"a\",
        \"expectedOutput\":\"b\"
    },
    {
        \"input\":\"c\",
        \"expectedOutput\":\"d\"
    }
]",
".py"
);



INSERT INTO stackCommand(name,compile,execute,sourcePostfix,templateCode,testcase,executePostfix)
VALUES (
"javascript",
"",
"node",
".js",
"function demo() {
  var result = process.argv[2];
  console.log(result);
}
demo();
",
"[
    {
        \"input\":\"a\",
        \"expectedOutput\":\"b\"
    },
    {
        \"input\":\"c\",
        \"expectedOutput\":\"d\"
    }
]",
".js"
);

INSERT INTO stackCommand(name,compile,execute,sourcePostfix,templateCode,testcase,executePostfix)
VALUES (
"c++",
"g++ -o demo",
"",
".cpp",
"#include <iostream>
using namespace std;
int main(int argc, char * argv[])
{
 cout<<\"Hello,World!\"<<endl;
 return 0;
}",
"[
    {
        \"input\":\"a\",
        \"expectedOutput\":\"b\"
    },
    {
        \"input\":\"c\",
        \"expectedOutput\":\"d\"
    }
]",
""
);