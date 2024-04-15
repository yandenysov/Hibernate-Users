package org.example.app.repository.impl;

import org.example.app.entity.User;
import org.example.app.repository.AppRepository;
import org.example.app.utils.Constants;
import org.example.app.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserRepository implements AppRepository<User> {

    @Override
    public String create(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            // HQL-запит.
            // :[parameter name] - іменований параметр (named parameter),
            // двокрапка перед іменем.
            String hql = "INSERT INTO User (name, email) " +
                    "VALUES (:name, :email)";
            // Створення HQL-запиту
            MutationQuery query = session.createMutationQuery(hql);
            // Формування конкретних значень для певного іменованого параметра
            query.setParameter("name", user.getName());
            query.setParameter("email", user.getEmail());
            // Виконання HQL-запиту
            query.executeUpdate();
            // Транзакція виконується
            transaction.commit();
            // Повернення повідомлення при безпомилковому
            // виконанні транзакції
            return Constants.DATA_INSERT_MSG;
        } catch (Exception e) {
            if (transaction != null) {
                // Відкочення поточної транзакції ресурсу
                transaction.rollback();
            }
            // Повернення повідомлення про помилку роботи з БД
            return e.getMessage();
        }
    }

    @Override
    public Optional<List<User>> read() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            // Транзакція стартує
            transaction = session.beginTransaction();
            // Формування колекції даними з БД через HQL-запит
            List<User> list =
                    session.createQuery("FROM User", User.class)
                            .list();
            // Транзакція виконується
            transaction.commit();
            // Повернення результату транзакції
            // Повертаємо Optional-контейнер з колецією даних
            return Optional.of(list);
        } catch (Exception e) {
            // Якщо помилка повертаємо порожній Optional-контейнер
            return Optional.empty();
        }
    }

    @Override
    public String update(User user) {
        // Спершу перевіряємо наявність об'єкта в БД за таким id.
        // Якщо ні, повертаємо повідомлення про відсутність таких даних,
        // інакше оновлюємо відповідний об'єкт в БД
        if (readById(user.getId()).isEmpty()) {
            return Constants.DATA_ABSENT_MSG;
        } else {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                // Транзакция стартует
                transaction = session.beginTransaction();
                // HQL-запит.
                // :[parameter name] - іменований параметр (named parameter),
                // двокрапка перед іменем.
                String hql = "UPDATE User SET name = :name, email = :email WHERE id = :id";
                // Створення HQL-запиту
                MutationQuery query = session.createMutationQuery(hql);
                // Формування конкретних значень для певного іменованого параметра
                query.setParameter("name", user.getName());
                query.setParameter("email", user.getEmail());
                query.setParameter("id", user.getId());
                // Виконання HQL-запиту
                query.executeUpdate();
                // Транзакція виконується
                transaction.commit();
                // Повернення повідомлення при безпомилковому
                // виконанні транзакції
                return Constants.DATA_UPDATE_MSG;
            } catch (Exception e) {
                if (transaction != null) {
                    // Відкочення поточної транзакції ресурсу
                    transaction.rollback();
                }
                // Повернення повідомлення про помилку роботи з БД
                return e.getMessage();
            }
        }
    }

    @Override
    public String delete(Long id) {
        // Спершу перевіряємо наявність об'єкта в БД за таким id.
        // Якщо ні, повертаємо повідомлення про відсутність таких даних,
        // інакше видаляємо відповідний об'єкт із БД
        if (readById(id).isEmpty()) {
            return Constants.DATA_ABSENT_MSG;
        } else {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                // Транзакція стартує
                transaction = session.beginTransaction();
                // HQL-запит.
                // :[parameter name] - іменований параметр (named parameter),
                // двокрапка перед іменем.
                String hql = "DELETE FROM User WHERE id = :id";
                // Створення HQL-запиту
                MutationQuery query = session.createMutationQuery(hql);
                // Формування конкретних значень для певного іменованого параметра
                query.setParameter("id", id);
                // Виконання HQL-запиту
                query.executeUpdate();
                // Транзакція виконується
                transaction.commit();
                // Повернення повідомлення при безпомилковому
                // виконанні транзакції
                return Constants.DATA_DELETE_MSG;
            } catch (Exception e) {
                if (transaction != null) {
                    // Відкочення поточної транзакції ресурсу
                    transaction.rollback();
                }
                // Повернення повідомлення про помилку роботи з БД
                return e.getMessage();
            }
        }
    }

    @Override
    public Optional<User> readById(Long id) {
        Transaction transaction = null;
        Optional<User> optional;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Транзакція стартує
            transaction = session.beginTransaction();
            // HQL-запит.
            // :[parameter name] - іменований параметр (named parameter),
            // двокрапка перед іменем.
            String hql = " FROM User u WHERE u.id = :id";
            // Створюємо запит
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("id", id);
            // Намагаємося отримати об'єкт за id
            optional = query.uniqueResultOptional();
            // Транзакція виконується
            transaction.commit();
            // Повернення результату транзакції
            // Повертаємо Optional-контейнер з об'єктом
            return optional;
        } catch (Exception e) {
            if (transaction != null) {
                // Відкочення поточної транзакції ресурсу
                transaction.rollback();
            }
            // Якщо помилка повертаємо порожній Optional-контейнер
            return Optional.empty();
        }
    }

    // Перевірка наявності об'єкту/сутності за певним id у БД
    private boolean isEntityWithSuchIdExists(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Перевірка наявності об'єкту за певним id
            user = session.get(User.class, user.getId());
            if (user != null) {
                Query<User> query = session.createQuery("FROM User", User.class);
                query.setMaxResults(1);
                query.getResultList();
            }
            return user != null;
        }
    }
}
