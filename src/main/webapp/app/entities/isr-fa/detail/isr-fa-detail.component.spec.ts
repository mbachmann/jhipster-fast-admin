import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IsrFaDetailComponent } from './isr-fa-detail.component';

describe('Component Tests', () => {
  describe('IsrFa Management Detail Component', () => {
    let comp: IsrFaDetailComponent;
    let fixture: ComponentFixture<IsrFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IsrFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ isr: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IsrFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IsrFaDetailComponent);
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
