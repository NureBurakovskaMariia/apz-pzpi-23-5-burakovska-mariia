import requests


# Приклад використання офіційного Steam Web API (ISteamNews)
# Демонструє взаємодію з сервісною частиною архітектури Steam

def get_game_news(appid, count=3):
    """
    Отримує останні новини для конкретної гри через Web API.
    """
    # Офіційна кінцева точка API
    url = "https://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/"

    params = {
        'appid': appid,  # ID додатка (наприклад, 440 для Team Fortress 2)
        'count': count,  # Кількість новин для отримання
        'format': 'json'  # Формат відповіді від сервера
    }

    try:
        # Виконання HTTP GET запиту до серверів Steam
        response = requests.get(url, params=params)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        return {"error": str(e)}


if __name__ == "__main__":
    # Тестовий запуск для отримання новин
    news = get_game_news(440)
    print(news)
