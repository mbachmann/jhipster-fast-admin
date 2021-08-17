import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FreeTextFaDetailComponent } from './free-text-fa-detail.component';

describe('Component Tests', () => {
  describe('FreeTextFa Management Detail Component', () => {
    let comp: FreeTextFaDetailComponent;
    let fixture: ComponentFixture<FreeTextFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FreeTextFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ freeText: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FreeTextFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FreeTextFaDetailComponent);
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
