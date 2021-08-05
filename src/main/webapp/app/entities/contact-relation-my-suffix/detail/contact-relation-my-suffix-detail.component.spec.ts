import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactRelationMySuffixDetailComponent } from './contact-relation-my-suffix-detail.component';

describe('Component Tests', () => {
  describe('ContactRelationMySuffix Management Detail Component', () => {
    let comp: ContactRelationMySuffixDetailComponent;
    let fixture: ComponentFixture<ContactRelationMySuffixDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContactRelationMySuffixDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contactRelation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContactRelationMySuffixDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactRelationMySuffixDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contactRelation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contactRelation).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
