namespace APZ_pz1
{
    internal class Program
    {
        /*
         Запит до шучного інтелекту:
        Напиши програму мовою C#, яка демонструє роботу структурного патерна проєктування Decorator на прикладі замовлення кави. 
        Створи інтерфейс ICoffee з методами string GetDescription() та double GetCost(). Реалізуй базовий клас SimpleCoffee, де опис — "Simple coffee", а ціна — 5.0. 
        Створи абстрактний клас CoffeeDecorator, який реалізує ICoffee, містить захищене поле _coffee та ініціалізує його через конструктор. 
        Методи мають бути віртуальними та викликати методи вкладеного об'єкта. 
        Реалізуй два конкретні декоратори. 
        MilkDecorator: додає до опису ", milk" та 1.5 до вартості. 
        ChocolateDecorator: додає до опису ", chocolate" та 2.0 до вартості. 
        У методі Main продемонструй створення об'єкта SimpleCoffee, який послідовно обгортається в обидва декоратори. 
        Виведи фінальний опис та загальну вартість у консоль.
         */

        public interface ICoffee
        {
            string GetDescription();
            double GetCost();
        }

        public class SimpleCoffee : ICoffee
        {
            public string GetDescription()
            {
                return "Simple coffee";
            }

            public double GetCost()
            {
                return 5.0;
            }
        }

        public abstract class CoffeeDecorator : ICoffee
        {
            protected ICoffee _coffee;

            public CoffeeDecorator(ICoffee coffee)
            {
                _coffee = coffee;
            }

            public virtual string GetDescription()
            {
                return _coffee.GetDescription();
            }

            public virtual double GetCost()
            {
                return _coffee.GetCost();
            }
        }

        public class MilkDecorator : CoffeeDecorator
        {
            public MilkDecorator(ICoffee coffee) : base(coffee) { }

            public override string GetDescription()
            {
                return _coffee.GetDescription() + ", milk";
            }

            public override double GetCost()
            {
                return _coffee.GetCost() + 1.5;
            }
        }

        public class ChocolateDecorator : CoffeeDecorator
        {
            public ChocolateDecorator(ICoffee coffee) : base(coffee) { }

            public override string GetDescription()
            {
                return _coffee.GetDescription() + ", chocolate";
            }

            public override double GetCost()
            {
                return _coffee.GetCost() + 2.0;
            }
        }


        static void Main(string[] args)
        {
            ICoffee coffee = new SimpleCoffee();
            coffee = new MilkDecorator(coffee);
            coffee = new ChocolateDecorator(coffee);

            Console.WriteLine(coffee.GetDescription());
            Console.WriteLine("Total cost: " + coffee.GetCost());
        }
    }
}
