import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
public class Main {
    public static ArrayList<Books> books = new ArrayList<>();
    public static ArrayList<Members> members = new ArrayList<>();
    public static ArrayList<BorrowBooks> borrowBooks = new ArrayList<>();
    public static ArrayList<BorrowBooks> readInLibrary = new ArrayList<>();
    public static ArrayList<BorrowBooks> readInLibrary2 = new ArrayList<>();
    public static ArrayList<Members> students = new ArrayList<>();
    public static ArrayList<Members> academics = new ArrayList<>();
    public static ArrayList<Books> printedbooks = new ArrayList<>();
    public static ArrayList<Books> handwrittenbooks = new ArrayList<>();
    public static ArrayList<BorrowBooks> extendbooks = new ArrayList<>();
    public static void main(String[] args) {
        String readFile = args[0];
        String writeFile = args[1];
        FileWriting.writeToFile(writeFile, "", false, false);
        String[] lines = FileReading.readFile(readFile);
        for (int i = 0; i < lines.length; i++) {
            String[] arr = (lines[i].split("\t"));
            if (arr[0].startsWith("addBook")) {
                int bookId = books.size() + 1;
                addBooks(arr[1], bookId , writeFile);
            }
            if (arr[0].startsWith("addMember")) {
                int memberId = members.size() + 1;
                addMembers(arr[1] , memberId , writeFile);
            }
            if (arr[0].startsWith("borrowBook")) {
                borrowBooks(Integer.parseInt(arr[1]) , Integer.parseInt(arr[2]) , LocalDate.parse(arr[3]) , writeFile);
            }
            if (arr[0].startsWith("readInLibrary")) {
                readInLibrary(Integer.parseInt(arr[1]) , Integer.parseInt(arr[2]) , LocalDate.parse(arr[3]) , writeFile);
            }
            if (arr[0].startsWith("returnBook")) {
                returnBooks(Integer.parseInt(arr[1]) , Integer.parseInt(arr[2]) , LocalDate.parse(arr[3]) , writeFile);
            }
            if (arr[0].startsWith("extendBook")) {
                extendBooks(Integer.parseInt(arr[1]) , Integer.parseInt(arr[2]) , LocalDate.parse(arr[3]) , writeFile);
            }
            if (arr[0].startsWith("getTheHistory")) {
                getTheHistory(writeFile);
            }
        }
    }
    public static void addBooks(String type , int bookId ,  String writeFile) {
        books.add(new Books(type , bookId));
        for (Books book : books) {
            if (bookId == book.getId()) {
                FileWriting.writeToFile(writeFile , String.valueOf(book) , true,true );
            }
        }
    }
    public static void addMembers(String type , int memberId , String writeFile) {
        members.add(new Members(type , members.size() + 1));
        for (Members member : members) {
            if (member.getId() == memberId) {
                FileWriting.writeToFile(writeFile , String.valueOf(member) , true,true );
            }
        }
    }
    public static void borrowBooks(int bookId , int memberId , LocalDate borrowTime , String writeFile) {
        Books book = null;
        for (Books b : books) {
            if (b.getId() == bookId) {
                book = b;
                break;
            }
        }
        if (book == null) {
            return;
        }
        if (!book.getType().equals("P")) {
            FileWriting.writeToFile(writeFile, "You cannot borrow this book!", true, true);
            return;
        }
        int count = 0;
        for (BorrowBooks borrowBooks : borrowBooks) {
            if (borrowBooks.getMemberId() == memberId) {
                count++;
            }
        }
        for (Members member : members) {
            if (member.getType().equals("S")) {
                if (count >= 2) {
                    FileWriting.writeToFile(writeFile, "You have exceeded the borrowing limit!" , true , true);
                    return;
                }
            } else {
                if (count >= 4) {
                    FileWriting.writeToFile(writeFile, "You have exceeded the borrowing limit!" , true , true);
                    return;
                }
            }
        }
        borrowBooks.add(new BorrowBooks(bookId , memberId , borrowTime));
        for (BorrowBooks borrowBooks : borrowBooks) {
            if (borrowBooks.getBookId() == bookId && borrowBooks.getMemberId() == memberId) {
                FileWriting.writeToFile(writeFile , String.valueOf(borrowBooks) , true ,true);
            }
        }
    }
    public static void readInLibrary(int bookId , int memberId , LocalDate borrowTime , String writeFile) {
        for (BorrowBooks borrow : borrowBooks) {
            if (borrow.getBookId() == bookId) {
                FileWriting.writeToFile(writeFile, "You can not read this book!", true, true);
                return;
            }
        }
        for (Books book : books) {
            if (book.getId() == bookId && book.getType().equals("P")) { // "P" type of book can be read in library by all members
                readInLibrary.add(new BorrowBooks(bookId, memberId, borrowTime));
                for (BorrowBooks borrowBook : readInLibrary) {
                    if (borrowBook.getBookId() == bookId && borrowBook.getMemberId() == memberId) {
                        FileWriting.writeToFile(writeFile, "The book [" + bookId + "] was read in library by member [" + memberId + "] at " + borrowTime, true, true);
                    }
                }
                return;
            } else if (book.getId() == bookId && book.getType().equals("H")) { // "H" type of book can only be read in library by academic members
                for (Members member : members) {
                    if (member.getId() == memberId) {
                        if (member.getType().equals("A")) {
                            readInLibrary.add(new BorrowBooks(bookId, memberId, borrowTime));
                            for (BorrowBooks borrowBook : readInLibrary) {
                                if (borrowBook.getBookId() == bookId && borrowBook.getMemberId() == memberId) {
                                    FileWriting.writeToFile(writeFile, "The book [" + bookId + "] was read in library by member [" + memberId + "] at " + borrowTime, true, true);
                                }
                            }
                            return;
                        } else {
                            FileWriting.writeToFile(writeFile, "Students can not read handwritten books!", true, true);
                            return;
                        }
                    }
                }
            }
        }
    }
    public static void returnBooks(int bookId , int memberId , LocalDate returnTime , String writeFile) {
        int fee = 0;
        for (BorrowBooks borrowBook : readInLibrary) {
            if (borrowBook.getBookId() == bookId && borrowBook.getMemberId() == memberId) {
                readInLibrary2.add(borrowBook);
                FileWriting.writeToFile(writeFile ,"The book [" + bookId + "] was returned by member [" + memberId + "] at " + returnTime + " Fee: 0" ,true,true );
            }
        }
        for (BorrowBooks borrowBook : borrowBooks) {
            if (borrowBook.getBookId() == bookId && borrowBook.getMemberId() == memberId) {
                readInLibrary2.add(borrowBook);
                for (Members member : members) {
                    if (member.getId() == borrowBook.getMemberId()) {
                        if (member.getType().equals("A")) {
                            LocalDate deadline = borrowBook.getBorrowTime().plusWeeks(2);
                            if (deadline.isBefore(returnTime)) {
                                long daysBetween = ChronoUnit.DAYS.between(deadline, returnTime);
                                fee = (int) daysBetween;
                                FileWriting.writeToFile(writeFile ,"The book [" + bookId + "] was returned by member [" + memberId + "] at " + returnTime + " Fee: " + fee ,true,true );
                            } else {
                                FileWriting.writeToFile(writeFile ,"The book [" + bookId + "] was returned by member [" + memberId + "] at " + returnTime + " Fee: " + fee ,true,true );
                            }
                        } else {
                            LocalDate deadline = borrowBook.getBorrowTime().plusWeeks(1);
                            if (deadline.isBefore(returnTime)) {
                                long daysBetween = ChronoUnit.DAYS.between(deadline, returnTime);
                                fee = (int) daysBetween;
                                FileWriting.writeToFile(writeFile ,"The book [" + bookId + "] was returned by member [" + memberId + "] at " + returnTime + " Fee: " + fee ,true,true );
                            } else {
                                FileWriting.writeToFile(writeFile ,"The book [" + bookId + "] was returned by member [" + memberId + "] at " + returnTime + " Fee: " + fee ,true,true );
                            }
                        }
                    }
                }
            }
        }
        readInLibrary.removeAll(readInLibrary2);
        borrowBooks.removeAll(readInLibrary2);
    }
    public static void extendBooks(int bookId , int memberId , LocalDate extendTime , String writeFile) {
        for (BorrowBooks borrowBook : borrowBooks) {
            if (bookId == borrowBook.getBookId() && memberId == borrowBook.getMemberId()) {
                for (Members member : members) {
                    if (member.getId() == memberId) {
                        if (member.getType().equals("A")) {
                            if (extendTime.isBefore(borrowBook.getBorrowTime().plusWeeks(2)) || extendTime.isEqual(borrowBook.getBorrowTime().plusWeeks(2))) {
                                if (!extendbooks.contains(borrowBook)) {
                                    LocalDate newborrowtime = borrowBook.getBorrowTime().plusWeeks(2);
                                    borrowBook.setBorrowTime(newborrowtime);
                                    extendbooks.add(borrowBook);
                                    FileWriting.writeToFile(writeFile , "The deadline of book [" + bookId + "] was extended by member [" + memberId + "] at " + extendTime,true,true);
                                    FileWriting.writeToFile(writeFile , "New deadline of book [" + bookId + "] is " + borrowBook.getBorrowTime().plusWeeks(2) ,true,true);
                                } else {
                                    FileWriting.writeToFile(writeFile , "You cannot extend the deadline!" , true,true);
                                }
                            }
                        } else {
                            if (extendTime.isBefore(borrowBook.getBorrowTime().plusWeeks(1)) || extendTime.isEqual(borrowBook.getBorrowTime().plusWeeks(1))) {
                                if (!extendbooks.contains(borrowBook)) {
                                    LocalDate newborrowtime = borrowBook.getBorrowTime().plusWeeks(1);
                                    borrowBook.setBorrowTime(newborrowtime);
                                    extendbooks.add(borrowBook);
                                    FileWriting.writeToFile(writeFile , "The deadline of book [" + bookId + "] was extended by member [" + memberId + "] at " + extendTime,true,true);
                                    FileWriting.writeToFile(writeFile , "New deadline of book [" + bookId + "] is " + borrowBook.getBorrowTime().plusWeeks(1) ,true,true);
                                } else {
                                    FileWriting.writeToFile(writeFile , "You cannot extend the deadline!" , true,true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static void getTheHistory(String writeFile) {
        FileWriting.writeToFile(writeFile , "History of library:\n" , true , true);
        for (Members member : members) {
            if(member.getType().equals("S")) {
                students.add(member);
            } else {
                academics.add(member);
            }
        }
        for (Books book : books) {
            if (book.getType().equals("P")) {
                printedbooks.add(book);
            } else {
                handwrittenbooks.add(book);
            }
        }
        FileWriting.writeToFile(writeFile , "Number of students: " + students.size(), true , true);
        for (Members student : students) {
            FileWriting.writeToFile(writeFile , "Student [id: " + student.getId() + "]" , true , true);
        }
        FileWriting.writeToFile(writeFile , "" , true , true);
        FileWriting.writeToFile(writeFile , "Number of academics: " + academics.size() , true ,true);
        for (Members academic : academics) {
            FileWriting.writeToFile(writeFile , "Academic [id: " + academic.getId() + "]" , true ,true);
        }
        FileWriting.writeToFile(writeFile , "" , true , true);
        FileWriting.writeToFile(writeFile , "Number of printed books: " + printedbooks.size() , true ,true);
        for (Books printed : printedbooks) {
            FileWriting.writeToFile(writeFile , "Printed [id: " + printed.getId() + "]" , true ,true);
        }
        FileWriting.writeToFile(writeFile , "" , true , true);
        FileWriting.writeToFile(writeFile , "Number of handwritten books: " + handwrittenbooks.size() , true ,true);
        for (Books handwritten : handwrittenbooks) {
            FileWriting.writeToFile(writeFile , "Handwritten [id: " + handwritten.getId() + "]" , true ,true);
        }
        FileWriting.writeToFile(writeFile , "" , true ,true);
        FileWriting.writeToFile(writeFile , "Number of borrowed books: " + borrowBooks.size() , true,true);
        for (BorrowBooks borrowBook : borrowBooks) {
            FileWriting.writeToFile(writeFile , String.valueOf(borrowBook) ,true,true);
        }
        FileWriting.writeToFile(writeFile , "" , true,true);
        FileWriting.writeToFile(writeFile , "Number of books read in library: " + readInLibrary.size(),true,true);
        for (BorrowBooks readbooks : readInLibrary) {
            FileWriting.writeToFile(writeFile , "The book [" + readbooks.getBookId() + "] was read in library by member [" + readbooks.getMemberId() + "] at " + readbooks.getBorrowTime() , true,true );
        }
    }
}