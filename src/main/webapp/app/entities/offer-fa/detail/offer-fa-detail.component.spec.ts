import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OfferFaDetailComponent } from './offer-fa-detail.component';

describe('Component Tests', () => {
  describe('OfferFa Management Detail Component', () => {
    let comp: OfferFaDetailComponent;
    let fixture: ComponentFixture<OfferFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OfferFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ offer: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OfferFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OfferFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load offer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.offer).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
