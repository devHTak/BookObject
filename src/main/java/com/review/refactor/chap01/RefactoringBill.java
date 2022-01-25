package com.review.refactor.chap01;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RefactoringBill {

    public String statement(Invoice invoice, Map<String, Play> plays) {
        Statement statement = new Statement();
        statement.createStatement(invoice, plays);
        return renderPlainText(statement, plays);
    }

    public String htmlStatement(Invoice invoice, Map<String, Play> plays) {
        Statement statement = new Statement();
        statement.createStatement(invoice, plays);
        return renderHtml(statement, plays);
    }

    private String renderPlainText(Statement statement, Map<String, Play> plays) {
        String result = "청구 내역 (고객명: " + statement.getCustomer() + ")\n";

        for(Performance performance: statement.getPerformances()) {
            result += performance.playFor(plays).getName() + ": $"
                    + (performance.amountFor() / 100)
                    + "(" + performance.getAudience() + "석)\n";
        }
        result += "총액: $" + (statement.getTotalAmount() / 100)+ "\n";
        result += "적립 포인트: " + + statement.getTotalVolumeCredits() +"점\n";

        return result;
    }

    private String renderHtml(Statement statement, Map<String, Play> plays) {
        String result = "<h1>청구 내역 (고객명: " + statement.getCustomer() + ")</h1>\n";
        result += "<table>\n";
        result += "<tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>";
        for(Performance performance: statement.getPerformances()) {
            result += "<tr><td>" + performance.playFor(plays).getName() + "</td>"
                    + "<td>" + (performance.amountFor() / 100) + "</td>"
                    + "<td>" + performance.getAudience() + "</td></tr>)\n";
        }
        result += "</table>\n";
        result += "<p>총액: $" + (statement.getTotalAmount() / 100)+ "</p>\n";
        result += "<p>적립 포인트: " + statement.getTotalVolumeCredits() +"점</p>\n";

        return result;
    }

}
