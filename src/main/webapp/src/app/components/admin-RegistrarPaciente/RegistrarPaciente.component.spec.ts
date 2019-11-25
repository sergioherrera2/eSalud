import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarUsuarioComponent } from './RegistrarUsuario.component';

describe('RegistrarUsuarioComponent', () => {
  let component: RegistrarUsuarioComponent;
  let fixture: ComponentFixture<RegistrarUsuarioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegistrarUsuarioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrarUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});