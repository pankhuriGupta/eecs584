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


public class ExtractExpressionLogicVisitor implements ExpressionVisitor{

	boolean isOrExpression = false;
	
	//TODO: keep state so dont need to do the work each time this is called.
	public boolean selectionPredicateIncludesOr(Expression exp){
		isOrExpression = false; 
		exp.accept(this);
		return isOrExpression;
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
//		System.out.println("AND EXPRESSION: " + andExpression);
		andExpression.getLeftExpression().accept(this);
		andExpression.getRightExpression().accept(this);
	}

	@Override
	public void visit(OrExpression orExpression) {
//		System.out.println("OR EXPRESSION: " + orExpression);
		this.isOrExpression = true;

	}

	@Override
	public void visit(Between between) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EqualsTo equalsTo) {

	}

	@Override
	public void visit(GreaterThan e) {

	}

	@Override
	public void visit(GreaterThanEquals e) {

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
	public void visit(LikeExpression e) {

	}

	@Override
	public void visit(MinorThan e) {

	}

	@Override
	public void visit(MinorThanEquals e) {

		
	}

	@Override
	public void visit(NotEqualsTo e) {

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
