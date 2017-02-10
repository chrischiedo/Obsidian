package Parser

sealed trait AST

/* Expressions */
case class Variable(x : String) extends AST
case class NumLiteral(value : Int) extends AST
case class Conjunction(e1 : AST, e2 : AST) extends AST
case class Disjunction(e1 : AST, e2 : AST) extends AST
case class LogicalNegation(e : AST) extends AST
case class Add(e1 : AST, e2 : AST) extends AST
case class Subtract(e1 : AST, e2 : AST) extends AST
case class Divide(e1 : AST, e2 : AST) extends AST
case class Multiply(e1 : AST, e2 : AST) extends AST
case class Equals(e1 : AST, e2 : AST) extends AST
case class GreaterThan(e1 : AST, e2 : AST) extends AST
case class GreaterThanOrEquals(e1 : AST, e2 : AST) extends AST
case class LessThan(e1 : AST, e2 : AST) extends AST
case class LessThanOrEquals(e1 : AST, e2 : AST) extends AST
case class NotEquals(e1 : AST, e2 : AST) extends AST
case class Dereference(e : AST, f : String) extends AST

/* statements and control flow constructs */
case object Return extends AST
case class ReturnExpr(e : AST) extends AST
case class Transition(newStateName : String) extends AST
case class Assignment(assignTo : AST, e : AST) extends AST
case class Throw() extends AST
case class If(eCond : AST, s : Seq[AST]) extends AST
case class IfThenElse(eCond : AST, s1 : Seq[AST], s2 : Seq[AST]) extends AST
case class TryCatch(s1 : Seq[AST], s2 : Seq[AST]) extends AST
case class Switch(e : AST, cases : Seq[SwitchCase]) extends AST
case class SwitchCase(stateName : String, body : Seq[AST]) extends AST

/* These can be both statements and expressions */
case class LocalInvocation(name : String, args : Seq[AST]) extends AST
case class Invocation(recipient : AST, name : String, args : Seq[AST]) extends AST
case class Construction(name : String, args : Seq[AST]) extends AST

sealed trait TypeModifier
case object IsFinal extends TypeModifier
case object IsLinear extends TypeModifier
case object IsUnique extends TypeModifier
case object IsShared extends TypeModifier
case class Type(modifiers : Seq[TypeModifier], name : String) extends AST

/* Declarations */
case class TypeDecl(name : String, typ : Type) extends AST

case class VarDecl(typ : Type, varName : String) extends AST

case class FuncDecl(name : String,
                           args : Seq[VarDecl],
                           body : Seq[AST]) extends AST
case class TransactionDecl(name : String,
                                  args : Seq[VarDecl],
                                  body : Seq[AST]) extends AST
case class StateDecl(name : String, declarations : Seq[AST]) extends AST
case class ContractDecl(name : String, declarations : Seq[AST]) extends AST
case class Program(contracts : Seq[ContractDecl]) extends AST