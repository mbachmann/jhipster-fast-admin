import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FreeTextDetailComponent } from './free-text-detail.component';

describe('Component Tests', () => {
  describe('FreeText Management Detail Component', () => {
    let comp: FreeTextDetailComponent;
    let fixture: ComponentFixture<FreeTextDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FreeTextDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ freeText: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FreeTextDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FreeTextDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load freeText on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.freeText).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
