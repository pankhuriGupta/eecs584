package com.relationalcloud.tsqlparser.visitors;

import com.relationalcloud.tsqlparser.expression.AllComparisonExpression;
import com.relationalcloud.tsqlparser.expression.AnyComparisonExpression;
import com.relationalcloud.tsqlparser.expression.CaseExpression;
import com.relationalcloud.tsqlparser.expression.DateValue;
import com.relationalcloud.tsqlparser.expression.DoubleValue;
import com.relationalcloud.tsqlparser.expression.Expression;
import com.relationalcloud.tsqlparser.expression.ExpressionVisitor;
import com.relationalcloud.tsqlparser.expression.Function;
import com.relationalcloud.tsqlparser.expression.InverseExpression;
import com.relationalcloud.tsqlparser.expression.JdbcParameter;
import com.relationalcloud.tsqlparser.expression.LongValue;
import com.relationalcloud.tsqlparser.expression.NullValue;
import com.relationalcloud.tsqlparser.expression.Parenthesis;
import com.relationalcloud.tsqlparser.expression.StringValue;
import com.relationalcloud.tsqlparser.expression.TimeValue;
import com.relationalcloud.tsqlparser.expression.TimestampValue;
import com.relationalcloud.tsqlparser.expression.WhenClause;
import com.relationalcloud.tsqlparser.expression.operators.arithmetic.Addition;
import com.relationalcloud.tsqlparser.expression.operators.arithmetic.Division;
import com.relationalcloud.tsqlparser.expression.operators.arithmetic.Multiplication;
import com.relationalcloud.tsqlparser.expression.operators.arithmetic.Subtraction;
import com.relationalcloud.tsqlparser.expression.operators.conditional.AndExpression;
import com.relationalcloud.tsqlparser.expression.operators.conditional.OrExpression;
import com.relationalcloud.tsqlparser.expression.operators.relational.Between;
import com.relationalcloud.tsqlparser.expression.operators.relational.EqualsTo;
import com.relationalcloud.tsqlparser.expression.operators.relational.ExistsExpression;
import com.relationalcloud.tsqlparser.expression.operators.relational.GreaterThan;
import com.relationalcloud.tsqlparser.expression.operators.relational.GreaterThanEquals;
import com.relationalcloud.tsqlparser.expression.operators.relational.InExpression;
import com.relationalcloud.tsqlparser.expression.operators.relational.IsNullExpression;
import com.relationalcloud.tsqlparser.expression.operators.relational.LikeExpression;
import com.relationalcloud.tsqlparser.expression.operators.relational.MinorThan;
import com.relationalcloud.tsqlparser.expression.operators.relational.MinorThanEquals;
import com.relationalcloud.tsqlparser.expression.operators.relational.NotEqualsTo;
import com.relationalcloud.tsqlparser.schema.Column;
import com.relationalcloud.tsqlparser.statement.select.SubSelect;


public class WherePartitionVisitor implements ExpressionVisitor {

  private Expression expression = null;

  public Expression getPartitionCondition(Expression where) {
    where.accept(this);
    return expression;
  }

  @Override
  public void visit(NullValue nullValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(Function function) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(InverseExpression inverseExpression) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(JdbcParameter jdbcParameter) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(DoubleValue doubleValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(LongValue longValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(DateValue dateValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(TimeValue timeValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(TimestampValue timestampValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(Parenthesis parenthesis) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(StringValue stringValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(Addition addition) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(Division division) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(Multiplication multiplication) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(Subtraction subtraction) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(AndExpression andExpression) {
    andExpression.getLeftExpression().accept(this);
    andExpression.getRightExpression().accept(this);
  }

  @Override
  public void visit(OrExpression orExpression) {
    orExpression.getLeftExpression().accept(this);
    orExpression.getRightExpression().accept(this);

  }

  @Override
  public void visit(Between between) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(EqualsTo equalsTo) {
    // if one between the left and right expression is not a Column then it's a
    // condition over one column compared to a single value
    // Example1: [table.]column = 7
    // Example2: [table.]column = "value"
    if (!(equalsTo.getLeftExpression() instanceof Column)
        || !(equalsTo.getRightExpression() instanceof Column)) {
      expression = equalsTo;
    }
  }

  @Override
  public void visit(GreaterThan greaterThan) {
    expression = greaterThan;
  }

  @Override
  public void visit(GreaterThanEquals greaterThanEquals) {
    expression = greaterThanEquals;

  }

  @Override
  public void visit(InExpression inExpression) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(IsNullExpression isNullExpression) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(LikeExpression likeExpression) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(MinorThan minorThan) {
    expression = minorThan;

  }

  @Override
  public void visit(MinorThanEquals minorThanEquals) {
    expression = minorThanEquals;

  }

  @Override
  public void visit(NotEqualsTo notEqualsTo) {
    expression = notEqualsTo;

  }

  @Override
  public void visit(Column tableColumn) {
    // TODO Auto-generated method stub
  }

  @Override
  public void visit(SubSelect subSelect) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(CaseExpression caseExpression) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(WhenClause whenClause) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ExistsExpression existsExpression) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(AllComparisonExpression allComparisonExpression) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(AnyComparisonExpression anyComparisonExpression) {
    // TODO Auto-generated method stub

  }

}
