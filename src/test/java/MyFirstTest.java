import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MyFirstTest {
    WebDriver driver;

    @Test
    public void test() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://rgs.ru");
        Thread.sleep(5000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("return window.stop");
//страхование
        WebElement dropdownBtn = driver.findElement(By.xpath("//li[contains(@class, 'dropdown')]/a[contains(text(),'Страхование')]"));
        dropdownBtn.click();
//дмс
        WebElement dmsBtn = driver.findElement(By.xpath("//a[contains(text(), 'ДМС') and not(contains(text(), 'Полис'))]"));
        dmsBtn.click();
//отправить заявку
        WebElement openForm = driver.findElement(By.xpath("//a[contains(text(), 'Отправить заявку')]"));
        openForm.click();

//ожидалка
        WebDriverWait wait = new WebDriverWait(driver, 5, 200);
        wait.until(ExpectedConditions.elementToBeClickable(By.className("modal-dialog")));

        //заполнение полей
        fillInputByName("Фамилия", "Горбулина");
        fillInputByName("Имя", "Алеся");
        fillInputByName("Отчество", "Викторовна");
        fillInputByName("Телефон", "9998521365");
        //fillInputByName("Предпочитаемая дата контакта", "18022020");
        fillInputByName("Эл. почта", "qwerty");
        fillInputComment("Comment", "Юра - лучший в мире преподаватель");


        //регион
        By selectxpath = By.xpath("//select[@name='Region']");
        WebElement element = driver.findElement(selectxpath);
        long start1 = System.currentTimeMillis();
        Select select = new Select(element);
        select.getOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        long start2 = System.currentTimeMillis();
        Arrays.asList(element.getText().split("\n"));
        long start3 = System.currentTimeMillis();
        System.out.println("get text foreah: " + (start2 - start1) + "\nget global text: " + (start3 - start2));
        select.selectByValue("46");

//обработка данных
        WebElement checkboxBtn = driver.findElement((By.xpath("//input[contains(@class, 'checkbox')]")));
        checkboxBtn.click();
        //отправка анкеты
        WebElement sendBtn = driver.findElement((By.xpath("//button[contains(text(), \"Отправить\")]")));
        sendBtn.click();


        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Телефон']/following::span[contains(@class, 'validation')]")));
        //checkError("Телефон", "некорректные данные");
        checkError("Эл. почта", "некорректные данные");
        driver.close();
        driver.quit();
    }

    public void fillInputByName(String name, String textToFill) {
        String template = "//*[text()='%s']/following::input[1]";
        String fullxpath = String.format(template, name);
        driver.findElement(By.xpath(fullxpath)).sendKeys(textToFill);
    }

    public void fillInputComment(String name, String textToFill) {
        String template = "//*[text()='Комментарии']/following::textarea[1]";
        String fullxpath = String.format(template, name);
        driver.findElement(By.xpath(fullxpath)).sendKeys(textToFill);
    }

    ////*[text()='Комментарии']/following::textarea[1]
    public void checkError(String name, String expectedText) {
        String template = "//*[text()='Эл. почта']/following::span[contains(@class, 'validation')]";
        String fullxpath = String.format(template, name);
        String actualText = driver.findElement(By.xpath(fullxpath)).getText();

        Assert.assertEquals(expectedText, actualText);
    }
}


