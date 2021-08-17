import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IsrDetailComponent } from './isr-detail.component';

describe('Component Tests', () => {
  describe('Isr Management Detail Component', () => {
    let comp: IsrDetailComponent;
    let fixture: ComponentFixture<IsrDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IsrDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ isr: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IsrDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IsrDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load isr on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.isr).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
