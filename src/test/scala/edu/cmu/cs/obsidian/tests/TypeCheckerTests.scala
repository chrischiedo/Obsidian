package edu.cmu.cs.obsidian.tests


import org.junit.Assert.{assertTrue, fail}
import org.junit.Test
import org.scalatest.junit.JUnitSuite

import edu.cmu.cs.obsidian.typecheck._
import edu.cmu.cs.obsidian.parser._

import scala.collection.mutable.ArrayBuffer

class TypeCheckerTests extends JUnitSuite {
    type LineNumber = Int
    private def runTest(file: String, expectedErrors: Seq[(Error, LineNumber)]): Unit = {
        var prog: Program = null
        try {
            prog = Parser.parseFileAtPath(file, printTokens = false)
        }
        catch {
            case p: Parser.ParseException =>
                val errMsg = p.message
                fail(s"Failed with parser message $errMsg")
        }

        val checker = new Checker()
        val errs = checker.checkProgram(prog)
        var remaining = new ArrayBuffer[(Error, LineNumber)]() ++ expectedErrors
        for (err <- errs) {
            val pred = (expected: (Error, LineNumber)) => {
                expected._1 == err && expected._2 == err.loc.line
            }
            val line = err.loc.line
            assertTrue(s"Nothing matches $err at line $line", remaining.exists(pred))
            val indexToRemove = remaining.indexOf((err, err.loc.line))
            remaining.remove(indexToRemove)
        }
        val msg = s"The following errors weren't found when checking: $remaining"
        assertTrue(msg, remaining.isEmpty)
    }

    @Test def basicTest(): Unit = {
        runTest("resources/tests/type_checker_tests/ExampleTypeFailure.obs",
            (SubTypingError(BoolType(), IntType()), 18)
                ::(WrongArityError(1, 0, "createC"), 19)
                ::(LeakReturnValueError("createC"), 19)
                ::(SubTypingError(BoolType(), IntType()), 20)
                ::Nil
        )
    }

    @Test def operationTest(): Unit = {
        runTest("resources/tests/type_checker_tests/SimpleOperations.obs",
            (SubTypingError(BoolType(), IntType()), 5)
                ::(SubTypingError(StringType(), IntType()), 6)
                ::(SubTypingError(BoolType(), IntType()), 7)
                ::(SubTypingError(StringType(), BoolType()), 8)
                ::(SubTypingError(IntType(), BoolType()), 9)
                ::(SubTypingError(IntType(), BoolType()), 9)
                ::Nil
        )
    }
    @Test def comparisonTest(): Unit = {
        runTest("resources/tests/type_checker_tests/SimpleComparisons.obs",
            (SubTypingError(BoolType(), IntType()), 5)
                ::(SubTypingError(BoolType(), IntType()), 6)
                ::(SubTypingError(StringType(), IntType()), 7)
                ::(SubTypingError(StringType(), IntType()), 8)
                ::(SubTypingError(BoolType(), IntType()), 9)
                ::(SubTypingError(BoolType(), IntType()), 9)
                ::Nil
        )
    }
    @Test def variableTest(): Unit = {

    }

    @Test def fieldsTest(): Unit = {
        runTest("resources/tests/type_checker_tests/CheckFields.obs",
            (StateSpecificSharedError(), 10)
                ::(StateSpecificReadOnlyError(), 11)
                ::(StateSpecificReadOnlyError(), 13)
                ::Nil
        )
    }

    @Test def assignmentTest(): Unit = {
        runTest("resources/tests/type_checker_tests/Assignment.obs",
            (SubTypingError(BoolType(), IntType()), 17)
                ::(SubTypingError(
                    OwnedRef(ContractType("C_Unique")),
                    SharedRef(ContractType("C_Shared"))),
                    19)
                ::(FieldUndefinedError(ContractType("C_Shared"), "f2"), 21)
                ::(FieldUndefinedError(ContractType("C_Shared"), "f3"), 22)
                ::(SubTypingError(
                    SharedRef(ContractType("C_Shared")),
                    SharedRef(StateType("C_Shared", "S"))),
                    23)
                ::(VariableUndefinedError("j"), 27)
                ::Nil
        )
    }
}
