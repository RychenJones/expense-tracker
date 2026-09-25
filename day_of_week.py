#!/usr/bin/env python3

from datetime import datetime


def main():
    date_text = input("Enter a date (YYYY-MM-DD): ").strip()

    try:
        date = datetime.strptime(date_text, "%Y-%m-%d")
        print(f"The day of the week is {date.strftime('%A')}.")
    except ValueError:
        print("Invalid date. Please use YYYY-MM-DD format.")


if __name__ == "__main__":
    main()