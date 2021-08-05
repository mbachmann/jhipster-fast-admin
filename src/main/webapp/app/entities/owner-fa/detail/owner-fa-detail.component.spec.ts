import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OwnerFaDetailComponent } from './owner-fa-detail.component';

describe('Component Tests', () => {
  describe('OwnerFa Management Detail Component', () => {
    let comp: OwnerFaDetailComponent;
    let fixture: ComponentFixture<OwnerFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OwnerFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ owner: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OwnerFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OwnerFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load owner on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.owner).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
