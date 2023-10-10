package by.toukach.walletservice.application.views;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Класс, содержащий сообщения для форм.
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewMessage {

  public static final String ACTION_LIST =
      "Выберите действие:\n1.Создать счет\n2.Действие над существующим счетом\n"
          + "3.История\n4.На главную\n5.Выйти";
  public static final String ADMIN_ACTION_LIST =
      "Выберите действие:\n1.Просмотреть логи\n2.Выйти";
  public static final String ACCOUNT_NUMBER = "Введите название счета";
  public static final String ENTRY =
      "Приветствуем!\nВыберите действие:\n1.Войти\n2.Зарегистрироваться\n3.Выйти";
  public static final String USER_DETAIL = "ID = %s, название = %s, баланс = %s";
  public static final String ACCOUNT_ACTION =
      "Выберите действие\n1.Кредит\n2.Дебит\n3.На главную\n4.Выход";
  public static final String WAITER = "Чтобы войти введите любой символ";
  public static final String ACCOUNTS_NOT_EXIST = "У вас нет счетов";
  public static final String ACCOUNT_CHOOSE = "Выберите счет:";
  public static final String WRONG_OPTION = "Выбран неверный вариант\n";
  public static final String LOGIN = "Введите логин";
  public static final String PASSWORD = "Введите пароль";
  public static final String SUM = "Введите сумму";
  public static final String TRANSACTION_ID =
      "Введите целое число, чтобы указать id транзакции";
  public static final String TABLE_HEADER =
      "ID транзакции | счет | тип транзакции | сумма | %n------------------------------%n";
  public static final String TABLE_ENTRY = "%s | %s | %s | %s | %n";
  public static final String LOG_TABLE_HEADER = "тип | сообщение | время | %n-------------------%n";
  public static final String LOG_TABLE_ENTRY = "%s | %s | %s | %n";
}
