import pygame
import random
import sys
import os
import numpy
from enum import Enum
from collections import namedtuple
from pathlib import Path

# Initialize Pygame
pygame.init()

# Constants
BLOCK_SIZE = 20
BASE_SPEED = 8

# Colors
WHITE = (255, 255, 255)
RED = (200, 0, 0)
GREEN = (0, 255, 0)
BLUE1 = (0, 0, 255)
BLUE2 = (0, 100, 255)
BLACK = (0, 0, 0)
GRAY = (128, 128, 128)

# Game States
class GameState(Enum):
    MENU = 1
    PLAYING = 2
    GAME_OVER = 3

# Direction enum
class Direction(Enum):
    RIGHT = 1
    LEFT = 2
    UP = 3
    DOWN = 4

Point = namedtuple('Point', 'x, y')

class SnakeGame:
    def __init__(self, w=800, h=600):
        self.w = w
        self.h = h
        # Ensure dimensions are multiples of BLOCK_SIZE
        self.w = (self.w // BLOCK_SIZE) * BLOCK_SIZE
        self.h = (self.h // BLOCK_SIZE) * BLOCK_SIZE
        self.display = pygame.display.set_mode((self.w, self.h))
        pygame.display.set_caption('Snake Game')
        self.clock = pygame.time.Clock()
        # Initialize sounds
        pygame.mixer.init()
        self.eat_sound = eat_sound
        self.crash_sound = crash_sound
        
        # Initialize fonts
        self.font = pygame.font.Font(None, 36)
        self.large_font = pygame.font.Font(None, 72)
        
        # High score
        self.high_score = 0
        self.load_high_score()
        
        # Game state
        self.state = GameState.MENU
        self.reset()

    def reset(self):
        self.direction = Direction.RIGHT
        self.head = Point((self.w//2) // BLOCK_SIZE * BLOCK_SIZE, 
                         (self.h//2) // BLOCK_SIZE * BLOCK_SIZE)
        self.snake = [
            self.head,
            Point(self.head.x-BLOCK_SIZE, self.head.y),
            Point(self.head.x-(2*BLOCK_SIZE), self.head.y)
        ]
        self.score = 0
        self.food = None
        self.speed = BASE_SPEED
        self._place_food()

    def _place_food(self):
        x = random.randint(0, (self.w-BLOCK_SIZE)//BLOCK_SIZE)*BLOCK_SIZE
        y = random.randint(0, (self.h-BLOCK_SIZE)//BLOCK_SIZE)*BLOCK_SIZE
        self.food = Point(x, y)
        if self.food in self.snake:
            self._place_food()

    def play_step(self):
        # 1. Collect user input
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == pygame.KEYDOWN:
                if self.state == GameState.MENU:
                    if event.key == pygame.K_SPACE:
                        self.state = GameState.PLAYING
                        self.reset()
                elif self.state == GameState.PLAYING:
                    if event.key == pygame.K_LEFT and self.direction != Direction.RIGHT:
                        self.direction = Direction.LEFT
                    elif event.key == pygame.K_RIGHT and self.direction != Direction.LEFT:
                        self.direction = Direction.RIGHT
                    elif event.key == pygame.K_UP and self.direction != Direction.DOWN:
                        self.direction = Direction.UP
                    elif event.key == pygame.K_DOWN and self.direction != Direction.UP:
                        self.direction = Direction.DOWN
                    elif event.key == pygame.K_p:
                        self.state = GameState.MENU
                elif self.state == GameState.GAME_OVER:
                    if event.key == pygame.K_SPACE:
                        self.state = GameState.PLAYING
                        self.reset()

        if self.state == GameState.PLAYING:
            # 2. Move
            self._move(self.direction)
            self.snake.insert(0, self.head)

            # 3. Check if game over
            if self._is_collision():
                self.crash_sound.play()
                if self.score > self.high_score:
                    self.high_score = self.score
                    self.save_high_score()
                self.state = GameState.GAME_OVER

            # 4. Place new food or move
            if self.head == self.food:
                self.score += 1
                self.eat_sound.play()
                self._place_food()
                # Increase speed every 5 points
                if self.score % 5 == 0:
                    self.speed += 1
            else:
                self.snake.pop()

        # 5. Update UI and clock
        self._update_ui()
        self.clock.tick(self.speed)

    def _is_collision(self):
        # Only check if snake hits itself
        if self.head in self.snake[1:]:
            return True
        return False

    def _update_ui(self):
        self.display.fill(BLACK)
        
        if self.state == GameState.MENU:
            self._draw_menu()
        elif self.state == GameState.PLAYING:
            self._draw_game()
        elif self.state == GameState.GAME_OVER:
            self._draw_game_over()
            
        pygame.display.flip()
        
    def _draw_menu(self):
        # Title
        title = self.large_font.render('SNAKE GAME', True, GREEN)
        title_rect = title.get_rect(center=(self.w/2, self.h/3))
        self.display.blit(title, title_rect)
        
        # Instructions
        start_text = self.font.render('Press SPACE to Start', True, WHITE)
        start_rect = start_text.get_rect(center=(self.w/2, self.h/2))
        self.display.blit(start_text, start_rect)
        
        # High Score
        high_score_text = self.font.render(f'High Score: {self.high_score}', True, WHITE)
        high_score_rect = high_score_text.get_rect(center=(self.w/2, self.h*2/3))
        self.display.blit(high_score_text, high_score_rect)
        
    def _draw_game(self):
        # Draw snake
        for pt in self.snake:
            pygame.draw.rect(self.display, BLUE1, pygame.Rect(pt.x, pt.y, BLOCK_SIZE, BLOCK_SIZE))
            pygame.draw.rect(self.display, BLUE2, pygame.Rect(pt.x+4, pt.y+4, 12, 12))
            
        # Draw food
        pygame.draw.rect(self.display, RED, pygame.Rect(self.food.x, self.food.y, BLOCK_SIZE, BLOCK_SIZE))
        
        # Draw score
        score_text = self.font.render(f'Score: {self.score}', True, WHITE)
        self.display.blit(score_text, [10, 10])
        
        # Draw high score
        high_score_text = self.font.render(f'High Score: {self.high_score}', True, WHITE)
        self.display.blit(high_score_text, [10, 40])
        
    def _draw_game_over(self):
        game_over_text = self.large_font.render('GAME OVER', True, RED)
        game_over_rect = game_over_text.get_rect(center=(self.w/2, self.h/3))
        self.display.blit(game_over_text, game_over_rect)
        
        score_text = self.font.render(f'Final Score: {self.score}', True, WHITE)
        score_rect = score_text.get_rect(center=(self.w/2, self.h/2))
        self.display.blit(score_text, score_rect)
        
        restart_text = self.font.render('Press SPACE to Play Again', True, WHITE)
        restart_rect = restart_text.get_rect(center=(self.w/2, self.h*2/3))
        self.display.blit(restart_text, restart_rect)

    def _move(self, direction):
        x = self.head.x
        y = self.head.y
        if direction == Direction.RIGHT:
            x = (x + BLOCK_SIZE) % self.w
        elif direction == Direction.LEFT:
            x = (x - BLOCK_SIZE) % self.w
        elif direction == Direction.DOWN:
            y = (y + BLOCK_SIZE) % self.h
        elif direction == Direction.UP:
            y = (y - BLOCK_SIZE) % self.h
        self.head = Point(x, y)
        
    def load_high_score(self):
        try:
            with open('high_score.txt', 'r') as f:
                self.high_score = int(f.read())
        except:
            self.high_score = 0
            
    def save_high_score(self):
        with open('high_score.txt', 'w') as f:
            f.write(str(self.high_score))

if __name__ == '__main__':
    # Create simple sounds directly
    pygame.mixer.init()
    
    # Create a simple beep sound for eating
    duration = 0.1  # seconds
    sample_rate = 44100
    samples = int(sample_rate * duration)
    t = numpy.linspace(0, duration, samples)
    eat_wave = numpy.sin(2 * numpy.pi * 440 * t) * 0.5
    # Create stereo sound (2 channels)
    stereo_eat_wave = numpy.zeros((samples, 2), dtype=numpy.float32)
    stereo_eat_wave[:, 0] = eat_wave  # Left channel
    stereo_eat_wave[:, 1] = eat_wave  # Right channel
    eat_sound = pygame.sndarray.make_sound(stereo_eat_wave.astype(numpy.int16))
    
    # Create a simple crash sound
    duration = 0.2  # seconds
    samples = int(sample_rate * duration)
    t = numpy.linspace(0, duration, samples)
    crash_wave = numpy.sin(2 * numpy.pi * 220 * t) * 0.5
    # Create stereo sound (2 channels)
    stereo_crash_wave = numpy.zeros((samples, 2), dtype=numpy.float32)
    stereo_crash_wave[:, 0] = crash_wave  # Left channel
    stereo_crash_wave[:, 1] = crash_wave  # Right channel
    crash_sound = pygame.sndarray.make_sound(stereo_crash_wave.astype(numpy.int16))
    
    game = SnakeGame()
    
    # Game loop
    while True:
        game.play_step()
            
    pygame.quit()