import java.time.LocalDate;
public class BorrowBooks{
    private int bookId;
    private int memberId;
    private LocalDate borrowTime;

    public BorrowBooks(int bookId , int memberId , LocalDate borrowTime) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowTime = borrowTime;
    }
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(LocalDate borrowTime) {
        this.borrowTime = borrowTime;
    }

    @Override
    public String toString() {
        return "The book [" + getBookId() + "] was borrowed by member [" + getMemberId() + "] at " + getBorrowTime();
    }
}